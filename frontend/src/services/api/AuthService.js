//Define access to provider's methods for authentication purpose

import { Provider } from "./utilities/Provider.js";
import {
  AUTH_SERVER_ENDPOINTS,
  TOKEN_HEADER_NAME,
} from "../../Constants/Constants.js";

export class AuthService {
  constructor(globalOptions = null) {
    globalOptions === null
      ? (this.provider = new Provider())
      : (this.provider = new Provider(globalOptions));
  }
  register(user) {
    return this.provider.post(AUTH_SERVER_ENDPOINTS.REGISTER, user);
  }
  authenticate(user) {
    return this.provider.post(AUTH_SERVER_ENDPOINTS.AUTHENTICATE, user);
  }
  refresh(token) {
    return this.provider.get(AUTH_SERVER_ENDPOINTS.REFRESH, {
      [TOKEN_HEADER_NAME]: token,
    });
  }
}
