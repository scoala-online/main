import { useEffect, useState } from 'react';
import { Table, Container, Col, Row, Spinner, Button } from 'react-bootstrap';
import { useHistory } from 'react-router-dom';
import CustomTitle from '../../../components/customTitle/CustomTitle.js';
import httpService from '../../../services/http.service.js';

export default function RolesPage() {
  // State
  const [roles, setRoles] = useState(undefined);
  const [isLoading, setIsLoading] = useState(false);

  const history = useHistory();

  /**
   * Retrieves the roles from the database.
   */
  useEffect(() => {
    setIsLoading(true);
    httpService.getAll('/roles', (response) => {
      setRoles(response.data);
      setIsLoading(false);
    });
  }, []);

  const historyBack = () => history.goBack();

  /**
   * Builds a table row for each of the roles retrieved from the database.
   */
  const buildRoles = () => {
    if (roles) {
      return roles.map((role) => (
        <tr>
          <td>{role.id}</td>
          <td>{role.name}</td>
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
          <CustomTitle value={'Roles'} hadDivider={true} />
        </Col>
      </Row>
      <Row>
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
            </tr>
          </thead>
          <tbody>
            {isLoading ? <Spinner animation="grow" /> : buildRoles()}
          </tbody>
        </Table>
      </Row>
    </Container>
  );
}
