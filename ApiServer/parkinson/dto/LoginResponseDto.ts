export class LoginResponse{

  public constructor(public access_token: string){}

  public serialize(){
    return JSON.stringify(this);
  }
}