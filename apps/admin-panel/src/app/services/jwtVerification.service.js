import { useEffect } from 'react';
import { withRouter, useLocation, useHistory } from 'react-router-dom';
import AuthService from './auth.service';

const parseJwt = (token) => {
  try {
    return JSON.parse(atob(token.split('.')[1]));
  } catch (e) {
    console.error('Error parsing the token.', e);
  }
};

const JwtVerification = (props) => {
  const location = useLocation();
  const history = useHistory();

  useEffect(() => {
    const user = AuthService.getUser();

    if (user && user.access_token) {
      const decodedJwt = parseJwt(user.access_token);

      console.log(new Date(decodedJwt.exp * 1000));
      if (decodedJwt.exp * 1000 < Date.now()) {
        AuthService.logout();
        history.push('/login');
      }
    }
  }, [location]);

  return <div></div>;
};

export default withRouter(JwtVerification);
