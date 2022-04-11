import { Route, Redirect } from 'react-router-dom';
import AuthService from '../../services/auth.service';

// A wrapper for <Route> that redirects to the login
// screen if you're not yet authenticated.
export default function LoginRoute({ children, ...rest }) {
  const isAuthenticated = AuthService.isAuthenticated();

  return (
    <Route
      {...rest}
      render={({ location }) =>
        !isAuthenticated ? (
          children
        ) : (
          <Redirect
            to={{
              pathname: '/home',
              state: { from: location },
            }}
          />
        )
      }
    />
  );
}
