package memo.net;

import com.jsloves.election.DTO.ImageInfoDTO;

import java.io.Serializable;
import java.util.ArrayList;

import support.io.model.ResponseBody;

/**
 * Created by juhyukkim on 2015. 11. 22..
 */
public class BoardListBody extends ResponseBody {
    public ArrayList<BoardDTO> custMemoList;

    public static class BoardDTO implements Serializable {
        public BoardDTO(BoardDTO dto){
            longDate = dto.longDate;
            admCd = dto.admCd;
            memoSeq = dto.memoSeq;         // 메모 Seq
            contents = dto.contents;
            tag = dto.tag;
            imgYn = dto.imgYn;
        }

        public long longDate;
        public String admCd;
        public String memoSeq;         // 메모 Seq
        public String contents;
        public String tag;
        public String imgYn;
        public ArrayList<ImageInfoDTO> imgFileList;

        public String imgShow;
    }
}
