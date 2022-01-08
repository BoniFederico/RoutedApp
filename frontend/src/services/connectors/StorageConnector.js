//It contains methods that will be used by VUE components

import { STORAGE_CHANGE_EVENT } from "../../Constants/Constants.js";

export function saveToStore(key, object) {
  window.localStorage[key] = object;
  window.dispatchEvent(
    new CustomEvent(STORAGE_CHANGE_EVENT, {
      detail: {
        storage: key,
      },
    })
  );
}
export function getFromStore(key) {
  return window.localStorage[key];
}
export function deleteFromStore(key) {
  delete window.localStorage[key];
}
export function isStored(key) {
  return key in window.localStorage ? true : false;
}
