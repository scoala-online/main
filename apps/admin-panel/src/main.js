const chalk = require('chalk');
const dotenv = require('dotenv');
const fs = require('fs');
const path = require('path');
const express = require('express');

dotenv.config();
const app = express();

const endpointsUtil = require('./app/util/endpoints.util');

// Constants
const SERVER_PORT = process.env.NX_SERVER_PORT || 8080;

// Static directory
app.use(
  express.static(
    path.join(__dirname, '/../../../apps/admin-panel/src/app/public/')
  )
);

// Files
const navbar = fs.readFileSync(
  path.join(
    __dirname,
    '/../../../apps/admin-panel/src/app/public/navbar/navbar.html'
  ),
  'utf-8'
);
const footer = fs.readFileSync(
  path.join(
    __dirname,
    '/../../../apps/admin-panel/src/app/public/footer/footer.html'
  ),
  'utf-8'
);
const home = fs.readFileSync(
  path.join(
    __dirname,
    '/../../../apps/admin-panel/src/app/public/home/home.html'
  ),
  'utf-8'
);
const endpoints = async (resource) => {
  let endpointPage = fs.readFileSync(
    path.join(
      __dirname,
      '/../../../apps/admin-panel/src/app/public/endpoints/endpoints.html'
    ),
    'utf-8'
  );
  endpointPage = endpointPage.replace('{{ Resource }}', resource);
  endpointPage = endpointPage.replace('{{ Data }}', await endpointsUtil.buildBody(resource));

  return endpointPage;
};

app.get('/', (req, res) => {
  res.send(navbar + home + footer);
});

app.get('/home', (req, res) => {
  res.redirect('/');
});

app.get('/endpoint/:resource', async (req, res) => {
  const requestedResource = req.params.resource;
  const content = await endpoints(requestedResource);
  res.send(navbar + content + footer);
});

const server = app.listen(SERVER_PORT, (error) => {
  if (error) console.log(error);

  console.log(
    chalk.green('Admin Panel Server started on port:'),
    server.address().port
  );
});
