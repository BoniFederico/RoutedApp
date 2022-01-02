/* We define a provider with the aim of decoupling access to the API from the technology used to do it:
if one day the axios library will change interfaces and we will have to update the program,
it will be sufficient to modify the access to the api from the provider while maintaining the same access interface. */

import axios from "axios";
import { handleResponse, handleError } from "./Response.js";
import { API_BASE_URL } from "../../../Constants/Constants.js";

export class Provider {
  //Define a provider with or without global http headers
  constructor(headers = null) {
    headers === null
      ? (this.provider = axios.create())
      : (this.provider = axios.create({ headers: headers }));
  }

  get(resource, headers = null) {
    return headers === null
      ? this.provider
          .get(API_BASE_URL.concat("/", resource))
          .then(handleResponse)
          .catch(handleError)
      : this.provider
          .get(API_BASE_URL.concat("/", resource), { headers: headers })
          .then(handleResponse)
          .catch(handleError);
  }
  delete(resource, id, headers = null) {
    return headers === null
      ? this.provider
          .delete(API_BASE_URL.concat("/", resource, "/", id))
          .then(handleResponse)
          .catch(handleError)
      : this.provider
          .delete(API_BASE_URL.concat("/", resource, "/", id), {
            headers: headers,
          })
          .then(handleResponse)
          .catch(handleError);
  }
  getSubset(resource, subsetId, headers = null) {
    return headers === null
      ? this.provider
          .get(API_BASE_URL.concat("/", resource, "/", subsetId))
          .then(handleResponse)
          .catch(handleError)
      : this.provider
          .get(API_BASE_URL.concat("/", resource, "/", subsetId), {
            headers: headers,
          })
          .then(handleResponse)
          .catch(handleError);
  }

  post(resource, data, headers = null) {
    return headers === null
      ? this.provider
          .post(API_BASE_URL.concat("/", resource), data)
          .then(handleResponse)
          .catch(handleError)
      : this.provider
          .post(API_BASE_URL.concat("/", resource), data, {
            headers: headers,
          })
          .then(handleResponse)
          .catch(handleError);
  }
  put(resource, data, headers = null) {
    return headers === null
      ? this.provider
          .put(API_BASE_URL.concat("/", resource), data)
          .then(handleResponse)
          .catch(handleError)
      : this.provider
          .put(API_BASE_URL.concat("/", resource), data, {
            headers: headers,
          })
          .then(handleResponse)
          .catch(handleError);
  }
}
