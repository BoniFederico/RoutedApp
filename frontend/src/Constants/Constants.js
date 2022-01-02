export const API_BASE_URL = "http://localhost:8080/routed/api";
export const TOKEN_HEADER_NAME = "Authorization";
export const TOKEN_REFRESH_HEADER_NAME = "Authorization_Refresh";

export const AUTH_SERVER_PATH = "user";
export const AUTH_SERVER_ENDPOINTS = {
  REGISTER: AUTH_SERVER_PATH.concat("/", "register"),
  AUTHENTICATE: AUTH_SERVER_PATH.concat("/", "authenticate"),
  REFRESH: AUTH_SERVER_PATH.concat("/", "refresh"),
};
export const RESOURCE_SERVER_PATH = "resource";
export const RESOURCE_SERVER_ENDPOINTS = {
  GET_ROUTES: RESOURCE_SERVER_PATH.concat("/", "routes"),
  DELETE_ROUTE: RESOURCE_SERVER_PATH.concat("/", "route"),
  SET_ROUTE: RESOURCE_SERVER_PATH.concat("/", "route"),
  UPDATE_ROUTE: RESOURCE_SERVER_PATH.concat("/", "route"),
  GET_INFO: RESOURCE_SERVER_PATH.concat("/", "userinfo"),
  GET_TYPES: RESOURCE_SERVER_PATH.concat("/", "types"),
};

export const STORED_ACCESS_TOKEN_NAME = "token";
export const STORED_REFRESH_TOKEN_NAME = "tokenRefresh";
export const STORED_GEOJSON_NAME = "routes";
export const STORAGE_CHANGE_EVENT = "store-change";
