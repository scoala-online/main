import ModuleContent from '../components/ModuleContent';

/**
 * MODULE PAGE
 *
 * ModulePage renders:
 * - ModuleContent component (from ../ModuleContent.js)
 */

const ModulePage = () => {
  const content_style = {
    padding: '10px 5.2vw',
    display: 'flex',
    width: '100%',
  };
  return (
    <div style={content_style}>
      <ModuleContent title="II. Studiu de caz" />
    </div>
  );
};

export default ModulePage;
