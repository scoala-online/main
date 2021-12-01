import { useHistory } from 'react-router-dom';
import { Button } from 'react-bootstrap';
import AuthService from '../../services/auth.service';

export default function HomePage(props) {
  const history = useHistory();

  const logoutHandler = () => {
    AuthService.logout();
    history.push('/login');
  };

  return (
    <div>
      <Button onClick={logoutHandler}>Logout</Button>
    </div>
  );
}
