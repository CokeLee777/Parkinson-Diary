import fcm from "firebase-admin";
// import serviceAccount from "../parkinson-diary-firebase-adminsdk.json";
export const fcmAdmin = fcm.initializeApp({
    credential: fcm.credential.cert({
        projectId: process.env.FCM_PROJECT_ID,
        clientEmail: process.env.FCM_CLIENT_EMAIL,
        privateKey: process.env.FCM_PRIVATE_KEY?.replace(/\\n/g, '\n')
    })
});