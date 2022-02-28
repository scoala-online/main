import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

import Card from 'react-bootstrap/Card';

export default function InfoColumns(props) {
  // Props
  const cardItems = props.cardItems;

  const cardStyle = {
    background: '#E0E0E0',
    height: '40vh',
  };

  return (
    <Container fluid>
      <Col>
        <Row>
          <Container fluid>
            <Row>
              {cardItems.map((item) => (
                <Col>
                  <Card className="text-center" style={cardStyle}>
                    <Card.Body style={{ padding: '0px' }}>
                      <Card.Title
                        style={{
                          // fontFamily: 'Open Sans',
                          fontStyle: 'normal',
                          fontWeight: 600,
                          fontSize: '1.5rem',
                          verticalAlign: 'middle',
                          padding: '3.5vh 2.8vw 3.9vh 2.8vw', // top, right, bottom, left
                        }}
                      >
                        {item.title}
                      </Card.Title>
                      <Card.Text
                        style={{
                          fontSize: '1.5rem',
                          fontStyle: 'normal',
                          fontWeight: 'normal',
                          textAlign: 'center',
                          padding: '0 1.4vw 3.5vh 1.4vw', // top, right, bottom, left
                        }}
                      >
                        {item.sub}
                      </Card.Text>
                    </Card.Body>
                  </Card>
                </Col>
              ))}
            </Row>
          </Container>
        </Row>
      </Col>
    </Container>
  );
}
