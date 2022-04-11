const httpUtil = {
  /**
   * Adds a path parameter to a given path.
   * @param {String} path - the current path.
   * @param {String} pathParam - the path parameter to be added to the path.
   * @returns {String} the path with the added parameter.
   */
  addPathParameter: (path, pathParam) => {
    if (path.endsWith('/')) {
      path = path.substr(0, path.length - 1);
    }

    if (pathParam.startsWith('/')) {
      pathParam = pathParam.substr(1, pathParam.length);
    }

    return `${path}/${pathParam}`;
  },
};

export default httpUtil;
