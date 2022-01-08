//It contains methods that will be used by VUE components

import { ResourceService } from "../api/ResourceService.js";
import { logOutFunction, refreshToken } from "./AuthConnector.js";
import { StatusCodes } from "http-status-codes";
import { getFromStore, saveToStore } from "./StorageConnector";
import { STORED_GEOJSON_NAME } from "../../Constants/Constants.js";

export async function setRoute(route, date, type, stages) {
  let cleanedStages = [];
  stages.forEach((stage) => {
    if (stage.name !== "") {
      cleanedStages.push(stage);
    }
  });

  let body = {
    route: route.replace(/(\r\n|\n|\r)/gm, ""),
    date: date,
    type: type,
    stages: JSON.stringify(cleanedStages),
  };

  let response = await new ResourceService()
    .setRoute(body)
    .then((res) => {
      return res;
    })
    .catch((res) => res);

  //try to silent refresh token
  if (response.status === StatusCodes.UNAUTHORIZED) {
    await refreshToken();

    response = await new ResourceService()
      .setRoute(body)
      .then((res) => {
        return res;
      })
      .catch((res) => res);
  }

  if (response.status === StatusCodes.UNAUTHORIZED) {
    logOutFunction();
  }

  return response.status;
}

export async function saveRouteFromRange(dates) {
  let datesArray = dates.split("/");
  let response = await new ResourceService()
    .getRoutesFromDates(datesArray[0], datesArray[1])
    .then((res) => {
      return res;
    })
    .catch((res) => res);

  if (response.status === StatusCodes.UNAUTHORIZED) {
    await refreshToken();
    response = await new ResourceService()
      .getRoutesFromDates(datesArray[0], datesArray[1])
      .then((res) => {
        return res;
      })
      .catch((res) => res);
  }
  if (response.status === StatusCodes.UNAUTHORIZED) {
    logOutFunction();
  }
  if (response.status === StatusCodes.OK) {
    response.response.forEach((r) => {
      if ("route" in r)
        if ("features" in r.route) {
          r.route.features.forEach((f) => {
            if ("properties" in f) {
              f.properties.routeid = r.id;
            }
          });
        }
    });
    saveToStore(STORED_GEOJSON_NAME, JSON.stringify(response.response));
  }

  return response.status;
}
export async function getUserInfo() {
  let response = await new ResourceService()
    .getInfo()
    .then((res) => {
      return res;
    })
    .catch((res) => res);

  if (response.status === StatusCodes.UNAUTHORIZED) {
    await refreshToken();
    response = await new ResourceService()
      .getInfo()
      .then((res) => {
        return res;
      })
      .catch((res) => res);
  }
  if (response.status === StatusCodes.UNAUTHORIZED) {
    logOutFunction();
    return response.status;
  }
  return response.response;
}
export async function getTypeList() {
  let response = await new ResourceService()
    .getTypes()
    .then((res) => {
      return res;
    })
    .catch((res) => res);
  if (response.status === StatusCodes.REQUEST_HEADER_FIELDS_TOO_LARGE) {
    return response.status;
  }
  if (response.status === StatusCodes.UNAUTHORIZED) {
    logOutFunction();

    return response.status;
  }
  let types = [];
  JSON.parse(response.response.value).forEach((t) => types.push(t.type));

  return types;
}
export async function updateRoute(route, date, type, stages, id) {
  let cleanedStages = [];
  stages.forEach((stage) => {
    if (stage.name !== "") {
      cleanedStages.push(stage);
    }
  });

  let body = {
    id: id,
    route: route.replace(/(\r\n|\n|\r)/gm, ""),
    date: date,
    type: type,
    stages: JSON.stringify(cleanedStages),
  };

  let response = await new ResourceService()
    .updateRoute(body)
    .then((res) => {
      return res;
    })
    .catch((res) => res);
  if (response.status === StatusCodes.UNAUTHORIZED) {
    await refreshToken();
    response = await new ResourceService()
      .updateRoute(body)
      .then((res) => {
        return res;
      })
      .catch((res) => res);
  }
  if (response.status === StatusCodes.UNAUTHORIZED) {
    logOutFunction();
  }

  return response.status;
}

export async function deleteRoute(id) {
  let response = await new ResourceService()
    .deleteRoute(id)
    .then((res) => {
      return res;
    })
    .catch((res) => res);
  if (response.status === StatusCodes.UNAUTHORIZED) {
    await refreshToken();
    response = await new ResourceService()
      .deleteRoute(id)
      .then((res) => {
        return res;
      })
      .catch((res) => res);
  }
  if (response.status === StatusCodes.UNAUTHORIZED) {
    logOutFunction();
  }

  return response.status;
}
