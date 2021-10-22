import styled from 'styled-components';
import { gradeSidebarButtonWidth, subjectSidebarButtonWidth, minSidebarButtonWidth } from './Sizes';

/**
 * Container for the content of a sidebar
 */
export const SidebarContainer = styled.div`
  background: ${({ type }) =>
    type === 'grade'
      ? 'linear-gradient(180deg, rgba(0, 201, 183, 0.9) 0%, rgba(0, 224, 206, 0.9) 100%)'
      : 'linear-gradient(319.97deg, #009C8E 7.3%, #00E0CE 100%)'};
  border-radius: 0px;

  display: flex;
  flex-direction: column;
  font-size: 1.5rem;

  position: fixed;
  width: ${({ type }) => (type === 'grade' ? gradeSidebarButtonWidth.toString() : subjectSidebarButtonWidth.toString())}vw;
  min-width: ${minSidebarButtonWidth}px;
  bottom: ${({ bottomVal }) => bottomVal};
  top: ${({ topVal }) => topVal};
  left: ${({ active, type, windowWidth }) => {
    let minActive = gradeSidebarButtonWidth * windowWidth / 100 < minSidebarButtonWidth;
    if (type === 'grade') {
      if (active) {
        return '0';
      }
      return !minActive ? -(gradeSidebarButtonWidth + 10).toString() + 'vw' : -minSidebarButtonWidth.toString() + 'px';
    }

    if (active) {
      return !minActive ? gradeSidebarButtonWidth.toString() + 'vw' : minSidebarButtonWidth.toString() + 'px';
    }
    return !minActive ? -(gradeSidebarButtonWidth + subjectSidebarButtonWidth).toString() + 'vw' : -2*minSidebarButtonWidth.toString() + 'px';
  }};
  z-index: ${({ type }) => (type === 'grade' ? '100' : '99')};

  transition: left ${({ type }) => (type === 'grade' ? '700ms' : '1000ms')}, top 350ms, bottom 350ms;

  direction: ${({ type }) => (type === 'grade' ? 'rtl' : 'ltr')};
  overflow-y: auto;
  overflow-x: hidden;
`;
