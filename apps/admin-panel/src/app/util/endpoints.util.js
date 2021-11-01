const axios = require('axios').default;

axios.defaults.baseURL = process.env.NX_API_URL || 'http://localhost:5000/';
axios.defaults.responseType = 'json';

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

const displayItem = (element) => {
    let content = '';
    for (const key of Object.keys(element)) {
        content += `
            <span>${key}: ${element[key]}</span><br>
        `;
    }
    return content;
}

const getItemTitle = (element) => {
    for (const key of Object.keys(element)) {
        if (key !== 'id') {
            return key;
        }
    }
}

module.exports = {
  buildBody,
};
