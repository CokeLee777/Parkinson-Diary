module.exports = class LoginResponse{
  constructor(accessToken){
    this.access_token = accessToken;
  }

  serialize(){
    return JSON.stringify(this);
  }
}