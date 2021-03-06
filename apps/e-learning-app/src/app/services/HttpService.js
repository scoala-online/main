import axios from 'axios';

import httpUtil from '../utilities/HttpUtil';

// Configuration
axios.defaults.baseURL = process.env.NX_API_URL;
axios.defaults.responseType = 'json';
let authToken = null;
let refreshToken = null;

// Interceptors
axios.interceptors.request.use(
  function (config) {
    // Do something before request is sent
    //console.log(config);
    if (authToken != null) {
      config.headers['Authorization'] = `Bearer ${authToken}`;
    }
    return config;
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  function (response) {
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response data
    // console.log(response)
    return response;
  },
  function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    return Promise.reject(error);
  }
);

// Handlers
const responseHandler = (response, onResponse) => {
  if (onResponse) {
    onResponse(response);
  }
};

const errorHandler = (error) => {
  if (error.response) {
    // The request was made and the server responded with a status code
    // that falls out of the range of 2xx
    console.log(error.response.data);
    console.log(error.response.status);
    console.log(error.response.headers);
  } else if (error.request) {
    // The request was made but no response was received
    // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
    // http.ClientRequest in node.js
    console.log(error.request);
  } else {
    // Something happened in setting up the request that triggered an Error
    console.log('Error', error.message);
  }
  console.log(error.config);
};

// Http Service
const http = {
  /**
   * Takes in the credential of a user and calls the API's login route.\
   * If the credentials were correct then the access and refresh tokens are saved in the service
   * and the `onLogin` callback is executed.
   * @param {String} username
   * @param {String} password
   * @param {Function} onLogin -> callback that gets executed once the user gets logged in.
   */
  login: (username, password, onLogin) => {
    axios
      .post('/users/login', { username, password })
      .then((response) => {
        authToken = response.data.access_token;
        refreshToken = response.data.refresh_token;
        if (onLogin) {
          onLogin();
        }
      })
      .catch((error) => errorHandler(error));
  },

  /**
   * Invalidates the access and refresh tokens.
   */
  logout: () => {
    authToken = null;
    refreshToken = null;
  },

  /**
   * Gets all resources of a type.\
   * \
   * Calls the api at the `resourceEndpoint` provided and executes the
   * `onResponse` callback when the response is received.\
   * \
   * The `resourceEndpoint` should contain the name of the resource
   * that is being requested.\
   * Example:
   * - '/students'
   * - '/notebooks'
   *
   * The `onResponse` callback is a `Function` that should take the
   * ***response*** `Object` as a parameter.
   * @param {String} resourceEndpoint
   * @param {Function} onResponse
   */
  getAll: (resourceEndpoint, onResponse) => {
    axios
      .get(resourceEndpoint)
      .then((response) => responseHandler(response, onResponse))
      .catch((error) => errorHandler(error));
  },

  /**
   * Gets a single resource.\
   * \
   * Calls the api at the `resourceEndpoint` combined with the
   * `resourceId` as a *path parameter* and executes the
   * `onResponse` callback when the response is received.\
   * \
   * The `resourceEndpoint` should contain the name of the resource
   * that is being requested.\
   * Example:
   * - '/students'
   * - '/notebooks'
   *
   * The `resourceId` should contain the id of the resource
   * that is being requested.\
   * Example:
   * - 'cc327880f-9252-444b-94c2-b3b28c006'
   *
   * The `onResponse` callback is a `Function` that should take the
   * ***response*** `Object` as a parameter.
   * @param {String} resourceEndpoint
   * @param {String} resourceId
   * @param {Function} onResponse
   */
  getById: (resourceEndpoint, resourceId, onResponse) => {
    const endpoint = httpUtil.addPathParameter(resourceEndpoint, resourceId);
    axios
      .get(endpoint)
      .then((response) => responseHandler(response, onResponse))
      .catch((error) => errorHandler(error));
  },

  /**
   * Creates a new resource.\
   * \
   * Calls the api at the `resourceEndpoint` with the provided `data` as a body,
   * and executes the `onResponse` callback when the response is received.\
   * \
   * The `resourceEndpoint` should contain the name of the resource
   * that is being requested.\
   * Example:
   * - '/students'
   * - '/notebooks'
   *
   * The `resourceId` should contain the id of the resource
   * that is being requested.\
   * Example:
   * - 'cc327880f-9252-444b-94c2-b3b28c006'
   *
   * The `onResponse` callback is a `Function` that should take the
   * ***response*** `Object` as a parameter.
   * @param {String} resourceEndpoint
   * @param {Object} data
   * @param {Function} onResponse
   */
  add: (resourceEndpoint, data, onResponse) => {
    axios
      .post(resourceEndpoint, data)
      .then((response) => responseHandler(response, onResponse))
      .catch((error) => errorHandler(error));
  },

  /**
   * Updates a resource.\
   * \
   * Calls the api at the `resourceEndpoint` combined with the
   * `resourceId` as a *path parameter*, and the provided `data` as a body;
   *  it then executes the `onResponse` callback when the response is received.\
   * \
   * The `resourceEndpoint` should contain the name of the resource
   * that is being requested.\
   * Example:
   * - '/students'
   * - '/notebooks'
   *
   * The `onResponse` callback is a `Function` that should take the
   * ***response*** `Object` as a parameter.
   * @param {String} resourceEndpoint
   * @param {Object} data
   * @param {Function} onResponse
   */
  update: (resourceEndpoint, resourceId, data, onResponse) => {
    const endpoint = httpUtil.addPathParameter(resourceEndpoint, resourceId);
    axios
      .patch(endpoint, data)
      .then((response) => responseHandler(response, onResponse))
      .catch((error) => errorHandler(error));
  },

  /**
   * Deletes a single resource.\
   * \
   * Calls the api at the `resourceEndpoint` combined with the
   * `resourceId` as a *path parameter* and executes the
   * `onResponse` callback when the response is received.\
   * \
   * The `resourceEndpoint` should contain the name of the resource
   * that is being requested.\
   * Example:
   * - '/students'
   * - '/notebooks'
   *
   * The `resourceId` should contain the id of the resource
   * that is being requested.\
   * Example:
   * - 'cc327880f-9252-444b-94c2-b3b28c006'
   *
   * The `onResponse` callback is a `Function` that should take the
   * ***response*** `Object` as a parameter.
   * @param {String} resourceEndpoint
   * @param {String} resourceId
   * @param {Function} onResponse
   */
  delete: (resourceEndpoint, resourceId, onResponse) => {
    const endpoint = httpUtil.addPathParameter(resourceEndpoint, resourceId);
    axios
      .delete(endpoint)
      .then((response) => responseHandler(response, onResponse))
      .catch((error) => errorHandler(error));
  },

  // Base axios functions
  customGet: axios.get,
  customPost: axios.post,
  customPatch: axios.patch,
  customDelete: axios.delete,
};

export default http;
