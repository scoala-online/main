import Video from '../moduleVideo/Video';
import { Row, Col, Container } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import style from './ContentGridStyles';

/**
 * MODULE PAGE
 *
 * ContentGrid renders:
 * - a grid of Video components (from ./moduleVideo/Video.js)
 *
 * Props:
 * - videos: Video (video components from ./moduleVideo/Video.js)
 */

const ContentGrid = (props) => {
  const { videos } = props;

  return (
    <Container style={style}>
      <Row lg="auto">
        {videos.map((video) => (
          <Col>
            <Video video={video} />
          </Col>
        ))}
      </Row>
    </Container>
  );
};

export default ContentGrid;
