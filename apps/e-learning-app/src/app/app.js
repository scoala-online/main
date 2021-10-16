import { BrowserRouter as Router, Switch } from 'react-router-dom';

import Layout from './components/layout/Layout';

export function App() {
  return (
    <Router>
      <Switch>
        <Layout>
        </Layout>
      </Switch>
    </Router>
  );
}
export default App;
