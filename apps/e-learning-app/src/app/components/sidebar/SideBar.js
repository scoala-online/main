import { calculateTop, calculateBottom } from '../../utilities/SidebarUtil';
import { SidebarContainer } from '../../themes/Containers';
import { SidebarButton } from '../../themes/Buttons';
import fontStyle from '../../themes/FontFamilies';
import { useEffect, useState } from 'react';
import http from '../../services/HttpService';

/**
 * SideBar renders:
 * - a list of buttons on the left side of the page
 *
 * Props:
 * - type: string ( the type of sidebar ( 'grade' / 'subject' ) )
 * - subjectPos: number ( position of the first element of the sidebar )
 * - active: boolean ( the sidebar's visibility )
 * - sideBarRef: object ( ref for the sidebar )
 * - onClickFunction function ( function for the onClick property of the buttons in the list )
 * - dimensions: object ( contains the current height and width of the viewport )
 * - fontSize: string ( font-size for the buttons' text )
 *
 * State:
 * - items: list ( contains the list of elements which will be displayed in the sidebar)
 *
 * API Calls:
 * - getAll( '/grades' ): gets the list of grades
 * - getAll( '/subject' ): gets the list of subjects
 */
export default function SideBar(props) {
  const [items, setItems] = useState([]);

  useEffect(() => {
    if (props.type === 'grade') {
      http.getAll('/grades', (response) => setItems(response.data));
    } else {
      http.getAll('/subjects', (response) => setItems(response.data));
    }
  }, []);

  return (
    <SidebarContainer
      type={props.type}
      pos={props.subjectPos}
      topVal={calculateTop(
        props.type,
        items.length,
        props.subjectPos,
        props.dimensions.height
      )}
      bottomVal={calculateBottom(
        props.type,
        items.length,
        props.subjectPos,
        props.dimensions.height
      )}
      windowWidth={props.dimensions.width}
      active={props.active}
      ref={props.sideBarRef}
    >
      {items.map((item, index) => {
        return (
          <SidebarButton
            key={index}
            type={props.type}
            last={Number(index) + 1 === Number(items.length)}
            windowHeight={props.dimensions.height}
            onClick={() => props.onClickFunction(index)}
          >
            <span
              style={{ ...fontStyle.body_semibold, fontSize: props.fontSize }}
            >
              {item.value}
            </span>
          </SidebarButton>
        );
      })}
    </SidebarContainer>
  );
}
