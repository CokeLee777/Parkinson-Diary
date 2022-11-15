import jwt from 'jsonwebtoken';

declare global {
    namespace Express {
        interface Request {
            decodedToken?: string | jwt.JwtPayload;
        }
    }
}