export class DiaryInfoResponse {

  public constructor(
      public sleep_start_time: string,
      public sleep_end_time: string,
      public take_times: Array<TakeTime>){}

  public serialize(){
    return JSON.stringify(this);
  }
}

type TakeTime = {
  take_time: string
}