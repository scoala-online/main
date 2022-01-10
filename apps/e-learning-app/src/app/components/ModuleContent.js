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

const ModuleContent = (props) => {
  const title = props.title;
  return (
    <>
      <Sidebar title={title} />
      <ContentGrid type="normal" label="Recommended" videos={allVideos} />
    </>
  );
};

export default ModuleContent;
