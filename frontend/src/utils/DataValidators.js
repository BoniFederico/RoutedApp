//It contains methods for front end data validation

const gjv = require("geojson-validation");

export function isRouteValid(route) {
  if (route === "") {
    return false;
  }
  try {
    return gjv.valid(JSON.parse(route));
  } catch (e) {
    return false;
  }
}

export function areStagesValid(stages) {
  let flag = true;
  stages.forEach((stage) => {
    if ("lat" in stage && "len" in stage && "name" in stage) {
      if (
        isNaN(stage.lat) ||
        stage.lat === "" ||
        isNaN(stage.len) ||
        stage.len === "" ||
        stage.name === ""
      ) {
        if (!(stage.lat === "" && stage.len === "" && stage.name === "")) {
          flag = false;
        }
      }
    } else {
      flag = false;
    }
  });
  return flag;
}

// from https://stackoverflow.com/questions/46155/whats-the-best-way-to-validate-an-email-address-in-javascript

export function isMailValid(email) {
  return String(email)
    .toLowerCase()
    .match(
      /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    );
}

export function isUsernameValid(username) {
  return String(username)
    .toLowerCase()
    .match(/^[a-zA-Z0-9]+$/);
}
