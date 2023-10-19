package ChessEulMelk.Logics;

import ChessEulMelk.Globals.Globals;

public class BoardState {

	private static final String START_POS = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	public static void setupStartPosition(Board board) {
		parseFENString(board, START_POS);
	}

	public static void parseFENString(Board board, String fenString) {
		long start = System.nanoTime();
		String[] strs = fenString.split(" ");
		String[] rows = strs[0].split("/");

		board.reset();
		for (int i = 0; i < Globals.BOARD_SIZE; ++i) {
			int j = 0, n = rows[i].length();
			for (int k = 0; k < n; ++k) {
				char ch = rows[i].charAt(k);
				if (Character.isDigit(ch)) {
					j += ch - '0';
				} else {
					board.setPiece(i, j++, Piece.getPiece(ch));
				}
			}
		}

		board.turn = strs[1].equals("w") ? PieceColor.WHITE : PieceColor.BLACK;

		for (int i = 0, n = strs[2].length(); i < n; ++i) {
			char ch = strs[2].charAt(i);

			if (ch == '-')
				break;

			board.setCastlingRight(Castling.getCastlingRight(ch), true);
		}

		if (!strs[3].equals("-")) {
			int row = getRow(strs[3]);
			int col = getCol(strs[3]);

			board.setEnPassantMask(row, col);
		}

		board.halfMoveClock = Integer.parseInt(strs[4]);
		board.fullMoveNumber = Integer.parseInt(strs[5]);
		long end = System.nanoTime();
		double time = (end - start) / 1000000.0;

		System.out.println("Parse Time: " + time + " ms!!");
	}

	public static String generateFENString(Board board) {
		String outputString = "";
		String castlingString = "";
		String enPassantString = "";

		int enPassantIndex = Long.numberOfLeadingZeros(board.getEnPassantMask());
		int enPassantRow = enPassantIndex / Globals.BOARD_SIZE, enPassantCol = enPassantIndex % Globals.BOARD_SIZE;

		for (int i = 0; i < Globals.BOARD_SIZE; ++i) {
			int count = 0;

			for (int j = 0; j < Globals.BOARD_SIZE; ++j) {
				if (board.getPiece(i, j) != Piece.NONE) {
					outputString += count > 0 ? count : "";
					count = 0;

					outputString += Globals.PIECE_LETTERS[board.getPiece(i, j).ordinal()];
				} else {
					++count;
				}
			}

			outputString += count > 0 ? count : "";
			outputString += i < Globals.BOARD_SIZE - 1 ? "/" : " ";
		}

		outputString += board.turn == PieceColor.WHITE ? "w " : "b ";

		castlingString += board.getCastlingRight(Castling.WHITE_KING_SIDE) ? "K" : "";
		castlingString += board.getCastlingRight(Castling.WHITE_QUEEN_SIDE) ? "Q" : "";
		castlingString += board.getCastlingRight(Castling.BLACK_KING_SIDE) ? "k" : "";
		castlingString += board.getCastlingRight(Castling.BLACK_QUEEN_SIDE) ? "q" : "";

		outputString += !castlingString.equals("") ? castlingString : "-";

		enPassantString = board.getEnPassantMask() == 0 ? "-"
				: Globals.FILES[enPassantCol] + Globals.RANKS[enPassantRow];

		outputString += " " + enPassantString + " " + board.halfMoveClock + " " + board.fullMoveNumber;

		return outputString;
	}

	private static int getRow(String square) {
		return '8' - square.charAt(1);
	}

	private static int getCol(String square) {
		return square.charAt(0) - 'a';
	}
}