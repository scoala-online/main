import VideosRow from './ContentGrid';
import Sidebar from './moduleSidebar/Sidebar';
import lectie1 from '../../assets/tmp/Module/Lectie1.png';
import lectie2 from '../../assets/tmp/Module/Lectie2.png';
import lectie3 from '../../assets/tmp/Module/Lectie3.png';
import lectie4 from '../../assets/tmp/Module/Lectie4.png';
import lectie5 from '../../assets/tmp/Module/Lectie5.png';
import lectie6 from '../../assets/tmp/Module/Lectie6.png';
import lectie7 from '../../assets/tmp/Module/Lectie7.png';
const VideoContent = () => {
  const allVideos = [
    {
      title: 'Froda in literatura interbelica ',
      thumbnail: lectie1,
      videoLink: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
      length: 208,
      channel: {
        name: 'Profesor Badea',
        link: 'https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g',
        image: 'https://picsum.photos/30/30',
      },
    },
    {
      thumbnail: lectie2,
      title: 'Froda in literatura interbelica',
      videoLink: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
      length: 208,
      channel: {
        name: 'Profesor Banescu',
        link: 'https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g',
        image: 'https://picsum.photos/30/30',
      },
    },
    {
      thumbnail: lectie3,
      title: 'Froda in literatura interbelica ',
      videoLink: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
      length: 208,
      channel: {
        name: 'Profesor Isabela',
        link: 'https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g',
        image: 'https://picsum.photos/30/30',
      },
    },
    {
      thumbnail: lectie4,
      title: "Ion Vinea: 'Manifest activist  catre tinerime'",
      videoLink: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
      length: 208,
      channel: {
        name: 'Profesor Badea',
        link: 'https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g',
        image: 'https://picsum.photos/30/30',
      },
    },
    {
      thumbnail: lectie5,
      title: "Tristea Tzara: 'Manifet despe amorul slab si amorul amar'",
      videoLink: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
      length: 208,
      channel: {
        name: 'Profesor Badea',
        link: 'https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g',
        image: 'https://picsum.photos/30/30',
      },
    },
    {
      thumbnail: lectie6,
      title: 'Punctuatia si justificarile ei sintactice si stilistice',
      videoLink: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
      length: 208,
      channel: {
        name: 'Profesor Badea',
        link: 'https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g',
        image: 'https://picsum.photos/30/30',
      },
    },
    {
      thumbnail: lectie7,
      title: 'Punctiatia si justificarile ei sintactice si stilistice',
      videoLink: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
      length: 208,
      channel: {
        name: 'Profesor Badea',
        link: 'https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g',
        image: 'https://picsum.photos/30/30',
      },
    },
  ];
  return (
    <>
      <Sidebar title="II. Studiu de caz" />
      <VideosRow type="normal" label="Recommended" videos={allVideos} />
    </>
  );
};

export default VideoContent;
