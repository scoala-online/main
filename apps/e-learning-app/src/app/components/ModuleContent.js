import ContentGrid from './ContentGrid';
import Sidebar from './moduleSidebar/Sidebar';
import { allVideos } from './MockData';
/**
 * MODULE PAGE
 * 
 * ModuleContent renders:
 * - the Sidebar component (from ./moduleSidebar/Sidebar.js)
 * - the ContentGrid component (from ./ContentGrid.js)
 */

const ModuleContent = () => {
  return (
    <>
      <Sidebar title="II. Studiu de caz" />
      <ContentGrid type="normal" label="Recommended" videos={allVideos} />
    </>
  );
};

export default ModuleContent;
