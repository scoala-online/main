import Video from './video/Video';
import {Row,Col} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

const VideosRow = (props) => {
  const { videos } = props;
  const videos_label_container_style = {
    padding: '40px 10px 40px 0',
    marginLeft: '17rem',
  };
  return (
    <div >
       <div style={videos_label_container_style}>
            <Row lg="auto" >
            {videos.map((video) => (
              <Col>
                <Video video={video} />
              </Col>
            ))}
            </Row>
       </div> 
     </div>
  );
};

export default VideosRow;
