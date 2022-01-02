export function handleResponse(response) {
  return {
    response: response.data,
    status: response.status,
    statusText: response.statusText,
  };
}

export function handleError(error) {
  /*  comments from AXIOS documentation:
  error has a "response" property if the request was made and the server responded with a status code
  that falls out of the range of 2xx */
  if (error.response) {
    return {
      response: error.response.data,
      status: error.response.status,
      statusText: error.response.statusText,
    };
  }
  // The request was made but no response was received
  // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
  // http.ClientRequest in node.js
  else if (error.request) {
    console.log(error.request);
  }
  // Something happened in setting up the request that triggered an Error
  else {
    console.log("Error", error.message);
  }
}
