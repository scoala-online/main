import { useEffect, useState } from 'react';
import { Table, Container, Col, Row, Spinner, Button } from 'react-bootstrap';
import { useHistory } from 'react-router-dom';
import CustomTitle from '../../../components/customTitle/CustomTitle.js';
import httpService from '../../../services/http.service.js';

export default function LecturesPage() {
  // State
  const [lectures, setLectures] = useState(undefined);
  const [isLoading, setIsLoading] = useState(false);

  const history = useHistory();

  /**
   * Retrieves the lectures from the database.
   */
  useEffect(() => {
    setIsLoading(true);
    httpService.getAll('/lectures', (response) => {
      setLectures(response.data);
      setIsLoading(false);
    });
  }, []);

  const historyBack = () => history.goBack();

  /**
   * Builds a table row for each of the lectures retrieved from the database.
   */
  const buildLectures = () => {
    if (lectures) {
      return lectures.map((lecture) => (
        <tr>
          <td>{lecture.id}</td>
          <td>{lecture.title}</td>
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
          <CustomTitle value={'Lectures'} hadDivider={true} />
        </Col>
      </Row>
      <Row>
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>ID</th>
              <th>Title</th>
            </tr>
          </thead>
          <tbody>
            {isLoading ? <Spinner animation="grow" /> : buildLectures()}
          </tbody>
        </Table>
      </Row>
    </Container>
  );
}
