import VideoContent from '../components/ModuleContent';
const ModulePage = () => {
  const content_style = {
    padding: '10px 5.2vw',
    display: 'flex',
    width : '100%',
  };
  return (
    <div style={content_style}>
      <VideoContent />
    </div>
  );
};

export default ModulePage;
