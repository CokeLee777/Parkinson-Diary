const jwt = require('jsonwebtoken');

exports.verifyToken = (request, response, next) => {
  try {
    const accessToken = request.header("ACCESS_TOKEN").substr(7);
    request.decodedToken = jwt.verify(accessToken, process.env.JWT_SECRET_KEY);
    return next();
  } catch (error) {
    if (error === jwt.TokenExpiredError) {
      return response.status(419).send('Token was expired');
    } else {
      return response.status(401).send('Invalid token');
    }
  }
}