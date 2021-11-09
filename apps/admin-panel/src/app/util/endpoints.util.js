const axios = require('axios').default;

// Axios default configuration
axios.defaults.baseURL = process.env.NX_API_URL;
axios.defaults.responseType = 'json';

/**
 * Takes in the name of a resource, queries the API for the given resource, and builds an HTML
 * body with the data received from the API.
 *
 * @param {String} resource The name of a resource endpoint.
 * @returns {String} the HTML body showcasing all the API entries for the given resource.
 */
const buildBody = async (resource) => {
  const response = await axios.get(resource);
  const items = response.data;

  let content = '<div class="accordion" id="data">';
  items.map((element, index) => {
    content += buildItem(element, index);
  });
  content += '</div>';
  return content;
};

/**
 * Takes in an **element** and constructs a '*Bootstrap Accordion item*' with its data.
 *
 * @param {Object} element an entry received from the API.
 * @param {Number} index
 * @returns {String} the HTML accordion item representing the provided **element**.
 */
const buildItem = (element, index) => {
  const itemTitle = getItemTitle(element);

  return `
    <div class="accordion-item">
        <h2 class="accordion-header" id="heading${index}">
            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse${index}" aria-expanded="true" aria-controls="collapse${index}">
                ${itemTitle}: ${element[itemTitle]}
            </button>
        </h2>
        <div id="collapse${index}" class="accordion-collapse collapse" aria-labelledby="heading${index}">
            <div class="accordion-body">
                <div class="info">
                    ${displayItem(element)}
                </div>
                <div class="actions">
                    <button type="button" class="btn btn-warning">Edit</button>
                    <button type="button" class="btn btn-info">Duplicate</button>
                    <button type="button" class="btn btn-danger">Delete</button>
                </div>
            </div>
        </div>
    </div>
    `;
};

/**
 * Takes in an **element** and constructs an HTML preview of its contents.
 *
 * @param {Object} element an entry received from the API.
 * @returns {String} a series of HTML spans, displaying the content of the **element**.
 */
const displayItem = (element) => {
  let content = '';
  for (const key of Object.keys(element)) {
    content += `
            <span>${key}: ${element[key]}</span><br>
        `;
  }
  return content;
};

/**
 * Takes in an **element** `Object`, searches through its keys and returns the first one that is not the ***id***.
 *
 * @param {Object} element an entry received from the API.
 * @returns {String} the key which represents the title of the given `Object`.
 */
const getItemTitle = (element) => {
  for (const key of Object.keys(element)) {
    if (key !== 'id') {
      return key;
    }
  }
};

module.exports = {
  buildBody,
};
