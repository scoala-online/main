import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

import Card from 'react-bootstrap/Card'

export default function InfoColumns(props) {
  // Props
  const cardItems = props.cardItems

  const cardStyle = {
    background: '#cccccc',
    height: '40vh'
  }

  return (
    <Container>
      <Col>
        <Row>
          <Container fluid>
            <Row>
              {cardItems.map(item => (
                <Col>
                  <Card className="text-center" style={cardStyle}>
                    <Card.Body>
                      <Card.Title style={{
                        // fontFamily: 'Open Sans',
                        fontStyle: 'normal',
                        fontWeight: 600,
                        fontSize: '1.5rem'
                      }}>
                        {item.title}
                      </Card.Title>
                      <Card.Text style={{
                        fontSize: '1.5rem',
                        fontStyle: 'normal',
                        fontWeight: 'normal',
                        textAlign: 'center'
                      }}>
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
  )
}
