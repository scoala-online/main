import { useHistory } from 'react-router-dom';
import { Button, Container, Row, Col } from 'react-bootstrap';
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
          />
        </Col>
        <Col>
          <CustomCard
            title="Grades"
            description="School grades that we support."
            buttonText="Manage Grades"
            buttonLink="/grades"
          />
        </Col>
      </Row>
      <Row>
        <Col>
          <CustomCard
            title="Videos"
            description="The lecture video information, containing the transcript, summary, and the relevant teacher data."
            buttonText="Manage Videos"
            buttonLink="/videos"
          />
        </Col>
      </Row>
      <Row>
        <Col xs={4}>
          <CustomCard
            title="Lectures"
            description="The available lectures."
            buttonText="Manage Lectures"
            buttonLink="/lectures"
          />
        </Col>
        <Col xs={8}>
          <CustomCard
            title="Lecture Materials"
            description="The exercises, documents, and other materials provided with the lecture."
            buttonText="Manage Lecture Materials"
            buttonLink="/lecture-materials"
          />
        </Col>
      </Row>
      <Row>
        <Col xs={9}>
          <CustomCard
            title="Users"
            description="The users of the system."
            buttonText="Manage Users"
            buttonLink="/users"
          />
        </Col>
        <Col xs={3}>
          <CustomCard
            title="Roles"
            description="The security roles."
            buttonText="Manage Roles"
            buttonLink="/roles"
          />
        </Col>
      </Row>
    </Container>
  );
}
