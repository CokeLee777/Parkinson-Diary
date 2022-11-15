
const patientNum = 121212;

export const TestDataConfig = {

  Patients: {
    patient_num: patientNum,
    patient_name: 'admin',
    sleep_start_time: new Date(`July 1, 1999, 22:00`),
    sleep_end_time: new Date(`July 1, 1999, 08:00`),
    user_id: 1
  },

  Medicine: {
    patient_num: patientNum,
    take_time: new Date(`July 1, 1999, 12:00`)
  },

  Survey: {
    abnormal_movement: true,
    medicinal_effect: false,
    patient_condition: 50,
    survey_time: new Date(),
    patient_num: patientNum
  }

}