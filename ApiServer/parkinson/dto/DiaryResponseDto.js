
module.exports = class DiaryInfoResponse {

  constructor(sleepStartTime, sleepEndTime, takeTimes){
    this.sleep_start_time = sleepStartTime;
    this.sleep_end_time = sleepEndTime;
    this.take_times = takeTimes;
  }

  serialize(){
    return JSON.stringify(this);
  }
}