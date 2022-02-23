import ModuleContent from '../../components/moduleContent/ModuleContent';
import style from './ModulePageStyles';

/**
 * MODULE PAGE
 *
 * ModulePage renders:
 * - ModuleContent component (from ../ModuleContent.js)
 */

const ModulePage = () => {
  return (
    <div style={style}>
      <ModuleContent title="II. Studiu de caz" />
    </div>
  );
};

export default ModulePage;
