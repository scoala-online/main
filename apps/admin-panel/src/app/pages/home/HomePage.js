import { useHistory } from 'react-router-dom';
import { Button, Container, Row, Col } from 'react-bootstrap';
import { homeCardStyle } from '../../themes/styles';
import AuthService from '../../services/auth.service';
import CustomCard from '../../components/customCard/CustomCard';

export default function HomePage(props) {
  const history = useHistory();

  const logoutHandler = () => {
    AuthService.logout();
    history.push('/login');
  };

  return (
    <Container>
      <Row style={{ display: 'flex', justifyContent: 'end' }}>
        <Button onClick={logoutHandler}>Logout</Button>
      </Row>
      <Row>
        <Col>
          <CustomCard
            title="Subjects"
            description="School subjects that we support."
            buttonText="Manage Subjects"
            buttonLink="/subjects"
            style={homeCardStyle}
          />
        </Col>
        <Col>
          <CustomCard
            title="Grades"
            description="School grades that we support."
            buttonText="Manage Grades"
            buttonLink="/grades"
            style={homeCardStyle}
          />
        </Col>
      </Row>
    </Container>
  );
}
