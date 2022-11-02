
exports.DiaryCreateRequest = class DiaryCreateRequest {

  constructor(sleepStartTime, sleepEndTime, takeTimes){
    this.sleepStartTime = sleepStartTime;
    this.sleepEndTime = sleepEndTime;
    this.takeTimes = takeTimes;
  }

  serialize(){
    return JSON.stringify(this);
  }
}