export const getLocalTime = () => {
    // 한국시간으로 변환
    const currTime = new Date();
    const utcTime = currTime.getTime() + (currTime.getTimezoneOffset() * 60 * 1000);
    const krCurrTime = new Date(utcTime + (9*60*60*1000));

    return krCurrTime;
}