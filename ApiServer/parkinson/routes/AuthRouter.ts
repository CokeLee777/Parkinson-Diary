import jwt from 'jsonwebtoken';
import { TokenExpiredError, InvalidTokenError, BindingTokenError } from '../error/CommonError';
import {NextFunction, Request, Response} from "express";
import {AppConfig} from '../config/AppConfig';
const patientService = AppConfig.patientService;

export const verifyToken = async (request: Request, response: Response, next: NextFunction) => {
  try {

    request.decodedToken = await verifyHeader(request);
    const patientNum = (<any>request.decodedToken).patientNum;
    // Redis DB에 엑세스 토큰이 존재하는지 확인
    await patientService.authorizeAccessToken(patientNum);

    return next();
  } catch (error: any | Error) {
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

async function verifyHeader(request: Request){
  const accessToken = request.header("ACCESS_TOKEN");
  if(accessToken === undefined){
    throw new BindingTokenError('토큰 정보가 존재하지 않습니다.');
  } else if(accessToken.substring(0, 7) != process.env.JWT_PREFIX){
    throw new InvalidTokenError('유효하지 않은 토큰입니다.');
  }

  return jwt.verify(accessToken.substring(7), String(process.env.JWT_SECRET_KEY));
}