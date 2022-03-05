import { useEffect, useState } from 'react';
import { Table, Container, Col, Row, Spinner, Button } from 'react-bootstrap';
import { useHistory } from 'react-router-dom';
import CustomTitle from '../../../components/customTitle/CustomTitle.js';
import httpService from '../../../services/http.service.js';

export default function UsersPage() {
  // State
  const [users, setUsers] = useState(undefined);
  const [isLoading, setIsLoading] = useState(false);

  const history = useHistory();

  /**
   * Retrieves the users from the database.
   */
  useEffect(() => {
    setIsLoading(true);
    httpService.getAll('/users', (response) => {
      setUsers(response.data);
      setIsLoading(false);
    });
  }, []);

  const historyBack = () => history.goBack();

  /**
   * Builds a table row for each of the users retrieved from the database.
   */
  const buildUsers = () => {
    if (users) {
      return users.map((user) => (
        <tr>
          <td>{user.id}</td>
          <td>{user.username}</td>
          <td>{user.name}</td>
          <td>{user.validated ? 'True' : 'False'}</td>
        </tr>
      ));
    }
    return [];
  };

  return (
    <Container>
      <Row>
        <Col
          md={{ span: 2, offset: 10 }}
          style={{ display: 'flex', justifyContent: 'end' }}
        >
          <Button onClick={historyBack} style={{ marginTop: '25px' }}>
            Back
          </Button>
        </Col>
      </Row>
      <Row>
        <Col>
          <CustomTitle value={'Users'} hadDivider={true} />
        </Col>
      </Row>
      <Row>
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>ID</th>
              <th>Username</th>
              <th>Name</th>
              <th>Validated</th>
            </tr>
          </thead>
          <tbody>
            {isLoading ? <Spinner animation="grow" /> : buildUsers()}
          </tbody>
        </Table>
      </Row>
    </Container>
  );
}
