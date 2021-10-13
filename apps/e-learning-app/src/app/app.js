import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import Layout from './components/Layout';

import './app.module.css';

export function App() {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Layout></Layout>
        </Switch>
      </Router>
    </div>
  );
}
export default App;
