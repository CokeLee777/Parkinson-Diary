import { JsonWebTokenError } from "jsonwebtoken";

export class InternalServerError extends Error {

  private code: string;

  public constructor(message: string) {
    super(message)
    this.code = 'ERR_INTERNAL_SERVER'
    this.name = 'InternalServerError'
    this.stack = `${this.message}\n${super.stack}`
  }
}

export class DatabaseConnectError extends Error {

  private code: string;

  public constructor(message: string) {
    super(message)
    this.code = 'ERR_DATABASE_CONNECT'
    this.name = 'DatabaseConnectError'
    this.stack = `${this.message}\n${super.stack}`
  }
}

export class InvalidInputTypeError extends Error {

  private code: string

  public constructor(message: string) {
    super(message)
    this.code = 'ERR_INVALID_INPUT_TYPE'
    this.name = 'InvalidInputTypeError'
    this.stack = `${this.message}\n${super.stack}`
  }
}

export class NotEnoughInputDataError extends Error {

  private code: string;

  public constructor(message: string) {
    super(message)
    this.code = 'ERR_INVALID_PATIENT_NUMBER'
    this.name = 'InvalidPatientNumberError'
    this.stack = `${this.message}\n${super.stack}`
  }
}

export class BindingTokenError extends JsonWebTokenError {

  private code: string;

  public constructor(message: string) {
    super(message)
    this.code = 'ERR_BINDING_TOKEN'
    this.name = 'BindingTokenError'
    this.stack = `${this.message}\n${super.stack}`
  }
}

export class InvalidTokenError extends JsonWebTokenError {

  private code: string;

  public constructor(message: string) {
    super(message)
    this.code = 'ERR_INVALID_TOKEN'
    this.name = 'InvalidTokenError'
    this.stack = `${this.message}\n${super.stack}`
  }
}

export class TokenExpiredError extends JsonWebTokenError {

  private code: string;

  public constructor(message: string) {
    super(message)
    this.code = 'ERR_TOKEN_EXPIRED'
    this.name = 'TokenExpiredError'
    this.stack = `${this.message}\n${super.stack}`
  }
}