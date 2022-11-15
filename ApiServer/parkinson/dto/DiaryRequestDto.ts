export class DiaryCreateRequest {

  private readonly _sleepStartTime: string;
  private readonly _sleepEndTime: string;
  private readonly _takeTimes: Array<TakeTime>;

  public constructor(sleepStartTime: string, sleepEndTime: string, takeTimes: Array<TakeTime>){
    this._sleepStartTime = sleepStartTime;
    this._sleepEndTime = sleepEndTime;
    this._takeTimes = takeTimes;
  }

  get sleepStartTime(): string {
    return this._sleepStartTime;
  }

  get sleepEndTime(): string {
    return this._sleepEndTime;
  }

  get takeTimes(): Array<TakeTime> {
    return this._takeTimes;
  }

  public serialize(){
    return JSON.stringify(this);
  }
}

type TakeTime = {
  take_time: string
}