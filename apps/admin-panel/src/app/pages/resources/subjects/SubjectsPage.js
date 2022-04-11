import { useEffect, useState } from 'react';
import { Table, Container, Col, Row, Spinner, Button } from 'react-bootstrap';
import { useHistory } from 'react-router-dom';
import CustomTitle from '../../../components/customTitle/CustomTitle.js';
import httpService from '../../../services/http.service.js';

export default function SubjectsPage() {
  // State
  const [subjects, setSubjects] = useState(undefined);
  const [isLoading, setIsLoading] = useState(false);

  const history = useHistory();

  /**
   * Retrieves the subjects from the database.
   */
  useEffect(() => {
    setIsLoading(true);
    httpService.getAll('/subjects', (response) => {
      setSubjects(response.data);
      setIsLoading(false);
    });
  }, []);

  const historyBack = () => history.goBack();

  /**
   * Builds a table row for each of the subjects retrieved from the database.
   */
  const buildSubjects = () => {
    if (subjects) {
      return subjects.map((subject) => (
        <tr>
          <td>{subject.id}</td>
          <td>{subject.value}</td>
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
          <CustomTitle value={'Subjects'} hadDivider={true} />
        </Col>
      </Row>
      <Row>
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>ID</th>
              <th>Value</th>
            </tr>
          </thead>
          <tbody>
            {isLoading ? <Spinner animation="grow" /> : buildSubjects()}
          </tbody>
        </Table>
      </Row>
    </Container>
  );
}
