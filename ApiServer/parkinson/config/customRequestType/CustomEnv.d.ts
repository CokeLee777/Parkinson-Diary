declare global {
    namespace NodeJS {
        interface Process {
            env: ProcessEnv
        }
        interface ProcessEnv {
            FCM_PRIVATE_KEY: string,
            INSTANCE_ID: string,
        }
    }
}