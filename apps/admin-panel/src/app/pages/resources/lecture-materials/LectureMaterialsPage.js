import { useEffect, useState } from 'react';
import { Table, Container, Col, Row, Spinner, Button } from 'react-bootstrap';
import { useHistory } from 'react-router-dom';
import CustomTitle from '../../../components/customTitle/CustomTitle.js';
import httpService from '../../../services/http.service.js';

export default function LectureMaterialsPage() {
  // State
  const [lectureMaterials, setLectureMaterials] = useState(undefined);
  const [isLoading, setIsLoading] = useState(false);

  const history = useHistory();

  /**
   * Retrieves the lecture materials from the database.
   */
  useEffect(() => {
    setIsLoading(true);
    httpService.getAll('/lecture-materials', (response) => {
      setLectureMaterials(response.data);
      setIsLoading(false);
    });
  }, []);

  const historyBack = () => history.goBack();

  /**
   * Builds a table row for each of the lecture materials retrieved from the database.
   */
  const buildLectureMaterials = () => {
    if (lectureMaterials) {
      return lectureMaterials.map((lectureMaterial) => (
        <tr>
          <td>{lectureMaterial.id}</td>
          <td>{lectureMaterial.document}</td>
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
          <CustomTitle value={'Lecture Materials'} hadDivider={true} />
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
            {isLoading ? <Spinner animation="grow" /> : buildLectureMaterials()}
          </tbody>
        </Table>
      </Row>
    </Container>
  );
}
