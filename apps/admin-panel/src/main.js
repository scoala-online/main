const chalk = require('chalk');
const fs = require('fs');
const path = require('path');
const express = require('express');

const app = express();

// Constants
const SERVER_PORT = process.env.NX_SERVER_PORT || 8080;

// Static directory
app.use(express.static(path.join(__dirname, '/../../../apps/admin-panel/src/app/public/')));

// Files
const navbar = fs.readFileSync(
  path.join(__dirname, '/../../../apps/admin-panel/src/app/public/navbar/navbar.html'),
  'utf-8'
);
const footer = fs.readFileSync(
  path.join(__dirname, '/../../../apps/admin-panel/src/app/public/footer/footer.html'),
  'utf-8'
);
const home = fs.readFileSync(
  path.join(__dirname, '/../../../apps/admin-panel/src/app/public/home/home.html'),
  'utf-8'
);

app.get('/', (req, res) => {
  res.send(navbar + home + footer);
});

const server = app.listen(SERVER_PORT, (error) => {
  if (error) console.log(error);

  console.log(
    chalk.green('Admin Panel Server started on port:'),
    server.address().port
  );
});
