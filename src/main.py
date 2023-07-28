import pygame
import sys

from board import Board
from pygame import mixer
from helper import MoveType
from helper import GameState

class Main:

    def __init__(self):
        pygame.init()
        
        self.screen = pygame.display.set_mode((800,800))
        pygame.display.set_caption("Chess")

        self.initial_position = "default"
        self.Board = Board(self.initial_position)

        # Create background
        self.bg = pygame.image.load("assets\\imgs\\bg_green.png")
        self.bg = pygame.transform.scale(self.bg,(800,800))
        
        # Slice pieces.png into separate pieces
        pieces = pygame.image.load("assets\\imgs\\pieces.png")
        piece_width = pieces.get_width() // 6
        piece_height = pieces.get_height() // 2

        self.imgs = []

        self.outline_color = (255,255,255,100)
        self.highlight_color = (255,255,30,127)
        self.move_hint_color = (255,0,0,127)
        self.capture_color = (0,255,0,127)


        # Set up array of imgs
        for i in range(12):
            row = i // 6
            col = i % 6

            piece = pieces.subsurface(pygame.Rect(col * piece_width, row * piece_height, piece_width, piece_height))
            piece = pygame.transform.scale(piece,(100,100))

            self.imgs.append(piece)
        
        # Set up dictionary of move types to sounds
        self.sounds = {
            MoveType.DEFAULT: mixer.Sound("assets\\sounds\\DEFAULT.wav"),
            MoveType.ENPASSANT: mixer.Sound("assets\\sounds\\CAPTURE.wav"),
            MoveType.CAPTURE: mixer.Sound("assets\\sounds\\CAPTURE.wav"),
            MoveType.CASTLE_KING_SIDE: mixer.Sound("assets\\sounds\\CASTLE.wav"),
            MoveType.CASTLE_QUEEN_SIDE: mixer.Sound("assets\\sounds\\CASTLE.wav"),
            MoveType.PROMOTION: mixer.Sound("assets\\sounds\\PROMOTION.wav")
        }


    def mainloop(self):
        # The piece that is being interacted with. Note that
        # this main.py will ONLY interact with a pieces pixel location
        clicked_piece = None
        clicked_x_col, clicked_y_row = None, None
        previous_x_col, previous_y_row = None, None
        move_hints = None
        show_hint = True

        play_sound = True

        board = self.Board.board
        imgs = self.imgs

        while True:
            self.screen.blit(self.bg,(0,0))

            if clicked_x_col and clicked_y_row:
                pygame.draw.rect(self.screen, self.outline_color, pygame.Rect(clicked_x_col,clicked_y_row,100,100))

            if previous_x_col and previous_y_row:
                pygame.draw.rect(self.screen, self.highlight_color, pygame.Rect(previous_x_col,previous_y_row,100,100))

            if move_hints and show_hint and clicked_piece:
                for pos in move_hints:
                    col, row, move_type = pos
                    if self.Board.are_enemies(board[row][col],clicked_piece) or move_type == MoveType.ENPASSANT:
                        pygame.draw.rect(self.screen,self.capture_color,pygame.Rect(col*100,row*100,100,100))
                    else:
                        pygame.draw.rect(self.screen,self.move_hint_color,pygame.Rect(col*100,row*100,100,100))

            
            for event in pygame.event.get():
                
                GAME_STATE = self.Board.GAME_STATE

                clicked_x_col, clicked_y_row = pygame.mouse.get_pos()
                clicked_x_col = clicked_x_col//100 * 100
                clicked_y_row = clicked_y_row//100 * 100
                
                # keyboard press
                if event.type == pygame.KEYDOWN:

                    # click 'm'
                    if event.key == pygame.K_m:
                        play_sound = not play_sound
                        sound_status = "unmuted" if play_sound else "muted"
                        print(f"AUDIO {sound_status}")

                    # click 'h'
                    if event.key == pygame.K_h:
                        show_hint = not show_hint
                        hint_status = "showing hints" if show_hint else "hiding hints"
                        print(f"{hint_status}")

                    # click 'r'
                    if event.key == pygame.K_r:
                        self.Board.setBoard(self.initial_position)
                        print(f"Board reset to {self.initial_position}")

                # click
                if event.type == pygame.MOUSEBUTTONDOWN and GAME_STATE == GameState.ACTIVE:

                    clicked_row = event.pos[1] // 100
                    clicked_col = event.pos[0] // 100

                    previous_x_col = clicked_col * 100
                    previous_y_row = clicked_row * 100

                    clicked_piece = board[clicked_row][clicked_col]
                    if clicked_piece:
                        move_hints = self.Board.legal_moves(clicked_piece,check_for_checkmate=True)
                    #print(f"{clicked_piece} was clicked at {clicked_col},{clicked_row}")
                
                # mouse motion
                elif event.type == pygame.MOUSEMOTION and GAME_STATE == GameState.ACTIVE:
                    
                    if clicked_piece:
                        clicked_piece.x = event.pos[0] - 40
                        clicked_piece.y = event.pos[1] - 50


                # release click
                elif clicked_piece and event.type == pygame.MOUSEBUTTONUP and GAME_STATE == GameState.ACTIVE:
                    x,y = event.pos
                    col, row = x//100, y//100

                    # user_valid_move calls move_piece which also updates pixel location
                    valid, move_type = self.Board.user_valid_move(clicked_piece,(col,row))
                    if valid: # user_valid move will move the piece automatically (should change)
                        if play_sound:
                            self.play_sound(move_type)
                        move_hints = None
                        clicked_piece.x = col * 100
                        clicked_piece.y = row * 100
                        
                    else: # Reset Piece
                        clicked_piece.x = clicked_piece.col * 100
                        clicked_piece.y = clicked_piece.row * 100
                        

                    clicked_piece = None
                    
                    previous_x_col = col * 100
                    previous_y_row = row * 100

                # quit
                elif event.type == pygame.QUIT:
                    pygame.quit()
                    sys.exit()

            for row in range(8):
                for col in range(8):
                    if board[row][col]:
                        piece = board[row][col]
                        index = piece.img_index
                        if piece != clicked_piece:
                            self.screen.blit(imgs[index],(piece.x,piece.y))

            if clicked_piece:
                self.screen.blit(imgs[clicked_piece.img_index],(clicked_piece.x,clicked_piece.y))

            pygame.display.update()

    def play_sound(self, move_type: MoveType) -> None:
        self.sounds[move_type].play()

main = Main()
main.mainloop()