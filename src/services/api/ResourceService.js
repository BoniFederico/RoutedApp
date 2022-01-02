//Define access to provider's methods for accessing resources

import { Provider } from "./utilities/Provider";
import { getFromStore } from "../connectors/StorageConnector";
import {
  RESOURCE_SERVER_ENDPOINTS,
  TOKEN_HEADER_NAME,
  STORED_ACCESS_TOKEN_NAME,
} from "../../Constants/Constants.js";

export class ResourceService {
  constructor(globalOptions = null) {
    globalOptions === null
      ? (this.provider = new Provider())
      : (this.provider = new Provider(globalOptions));
  }
  getRoutes() {
    let response = this.provider.get(RESOURCE_SERVER_ENDPOINTS.GET_ROUTES, {
      [TOKEN_HEADER_NAME]: getFromStore(STORED_ACCESS_TOKEN_NAME),
    });
    return response;
  }
  deleteRoute(id) {
    let response = this.provider.delete(
      RESOURCE_SERVER_ENDPOINTS.DELETE_ROUTE,
      id,
      {
        [TOKEN_HEADER_NAME]: getFromStore(STORED_ACCESS_TOKEN_NAME),
      }
    );
    return response;
  }
  getTypes() {
    return this.provider.get(RESOURCE_SERVER_ENDPOINTS.GET_TYPES);
  }
  getInfo() {
    return this.provider.get(RESOURCE_SERVER_ENDPOINTS.GET_INFO, {
      [TOKEN_HEADER_NAME]: getFromStore(STORED_ACCESS_TOKEN_NAME),
    });
  }
  setRoute(route) {
    return this.provider.post(RESOURCE_SERVER_ENDPOINTS.SET_ROUTE, route, {
      [TOKEN_HEADER_NAME]: getFromStore(STORED_ACCESS_TOKEN_NAME),
    });
  }
  getRoute(id) {
    let response = this.provider.getSubset(
      RESOURCE_SERVER_ENDPOINTS.GET_ROUTES,
      id,
      { [TOKEN_HEADER_NAME]: getFromStore(STORED_ACCESS_TOKEN_NAME) }
    );
    return response;
  }
  updateRoute(route) {
    return this.provider.put(RESOURCE_SERVER_ENDPOINTS.UPDATE_ROUTE, route, {
      [TOKEN_HEADER_NAME]: getFromStore(STORED_ACCESS_TOKEN_NAME),
    });
  }
  getRoutesFromDates(date1, date2) {
    let response = this.provider.getSubset(
      RESOURCE_SERVER_ENDPOINTS.GET_ROUTES,
      date1.concat("/", date2),
      { [TOKEN_HEADER_NAME]: getFromStore(STORED_ACCESS_TOKEN_NAME) }
    );
    return response;
  }
}
