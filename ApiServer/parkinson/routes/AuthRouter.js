const jwt = require('jsonwebtoken');
const { TokenExpiredError, InvalidTokenError, BindingTokenError } = require('../error/CommonError');

exports.verifyToken = (request, response, next) => {
  try {
    request.decodedToken = verifyToken(request);
    return next();
  } catch (error) {
    if (error instanceof TokenExpiredError) {
      return response.status(419).json({message: '토큰이 만료되었습니다.'});
    } else if(error instanceof InvalidTokenError){
      return response.status(401).json({message: error.message});
    } else if(error instanceof jwt.JsonWebTokenError){
      return response.status(401).json({message: '변조된 토큰입니다.'});
    } else {
      return response.status(500).json({message: error.message});
    }
  }
}

function verifyToken(request){
  const accessToken = request.header("ACCESS_TOKEN");
  if(accessToken === undefined){
    throw new BindingTokenError('토큰 정보가 존재하지 않습니다.');
  } else if(accessToken.substring(0, 7) != process.env.JWT_PREFIX){
    throw new InvalidTokenError('유효하지 않은 토큰입니다.');
  }

  return jwt.verify(accessToken.substring(7), process.env.JWT_SECRET_KEY);
}