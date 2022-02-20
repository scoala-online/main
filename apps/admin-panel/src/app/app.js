import { BrowserRouter as Router, Switch } from 'react-router-dom';
import LoginRoute from './components/loginRoute/LoginRoute';
import PrivateRoute from './components/privateRoute/PrivateRoute';
import JwtVerification from './services/jwtVerification.service';
import HomePage from './pages/home/HomePage';
import LoginPage from './pages/login/LoginPage';
import GradesPage from './pages/resources/grades/GradesPage';
import SubjectsPage from './pages/resources/subjects/SubjectsPage';
import LecturesPage from './pages/resources/lectures/LecturesPage';
import LectureMaterialsPage from './pages/resources/lecture-materials/LectureMaterialsPage';
import VideosPage from './pages/resources/videos/VideosPage';
import RolesPage from './pages/resources/roles/RolesPage';
import UsersPage from './pages/resources/users/UsersPage';


export function App() {
  return (
    <Router>
      <Switch>
        <PrivateRoute exact path={['/home', '/']}>
          <HomePage />
        </PrivateRoute>
        <PrivateRoute exact path={['/grades']}>
          <GradesPage />
        </PrivateRoute>
        <PrivateRoute exact path={['/subjects']}>
          <SubjectsPage />
        </PrivateRoute>
        <PrivateRoute exact path={['/lectures']}>
          <LecturesPage />
        </PrivateRoute>
        <PrivateRoute exact path={['/lecture-materials']}>
          <LectureMaterialsPage />
        </PrivateRoute>
        <PrivateRoute exact path={['/videos']}>
          <VideosPage />
        </PrivateRoute>
        <PrivateRoute exact path={['/users']}>
          <UsersPage />
        </PrivateRoute>
        <PrivateRoute exact path={['/roles']}>
          <RolesPage />
        </PrivateRoute>
        <LoginRoute exact path="/login">
          <LoginPage />
        </LoginRoute>
      </Switch>
      <JwtVerification />
    </Router>
  );
}
export default App;
