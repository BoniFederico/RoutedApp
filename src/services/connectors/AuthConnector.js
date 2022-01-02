//It contains methods that will be used by VUE components

import { AuthService } from "../api/AuthService.js";
import {
  STORED_ACCESS_TOKEN_NAME,
  STORED_REFRESH_TOKEN_NAME,
  TOKEN_HEADER_NAME,
  STORED_GEOJSON_NAME,
  TOKEN_REFRESH_HEADER_NAME,
} from "../../Constants/Constants.js";
import {
  saveToStore,
  getFromStore,
  deleteFromStore,
  isStored,
} from "./StorageConnector.js";
import { StatusCodes } from "http-status-codes";

export function logOutFunction() {
  deleteFromStore(STORED_ACCESS_TOKEN_NAME);
  deleteFromStore(STORED_REFRESH_TOKEN_NAME);
  deleteFromStore(STORED_GEOJSON_NAME);

  return null;
}

export function isLoggedFunction() {
  return isStored(STORED_ACCESS_TOKEN_NAME);
}

export async function registerFunction(user) {
  let response = await new AuthService()
    .register(user)
    .then((res) => {
      return res;
    })
    .catch((res) => res);
  return response.status;
}
export async function refreshToken() {
  let response = await new AuthService()
    .refresh(getFromStore(STORED_REFRESH_TOKEN_NAME))
    .then((res) => {
      return res;
    })
    .catch((res) => res);
  if (response.status === StatusCodes.OK) {
    saveToStore(STORED_ACCESS_TOKEN_NAME, response.response[TOKEN_HEADER_NAME]);
  }
  return response.status;
}
export async function authenticateFunction(user) {
  let content = {
    username: "",
    password: "",
    email: "",
  };

  content.password = user.password;
  user.identifier.includes("@")
    ? (content.email = user.identifier)
    : (content.username = user.identifier);

  let response = await new AuthService()
    .authenticate(content)
    .then((res) => {
      return res;
    })
    .catch((res) => res);
  switch (response.status) {
    case StatusCodes.OK:
      saveToStore(
        STORED_ACCESS_TOKEN_NAME,
        response.response[TOKEN_HEADER_NAME]
      );
      saveToStore(
        STORED_REFRESH_TOKEN_NAME,
        response.response[TOKEN_REFRESH_HEADER_NAME]
      );
      return response.status;
    default:
      return response.status;
  }
}
